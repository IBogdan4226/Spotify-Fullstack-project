from passlib.context import CryptContext
from spyne import Application, rpc, ServiceBase, Integer
from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication
from jwtUtils import *
from repositories.role_repository import *
from repositories.user_repository import *
from repositories.user_roles_repository import *

blacklist = set()
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


class IDMService(ServiceBase):

    @rpc(str, str, _returns=str)
    def create_user(ctx, username, password):
        print("Creating user")
        returnV=create_user(username, pwd_context.hash(password))
        if(returnV==False):
            return 'false';
        return str(returnV)


    @rpc(str, str, _returns=str)
    def login(ctx, username, password):
        user = get_user_by_name(username)
        if user is None:
            ctx.transport.resp_status = 401
            return 'User or password doesnt match'
        if not pwd_context.verify(password, user.password):
            ctx.transport.resp_status = 401
            return 'User or password doesnt match'
        return createJwt(user)

    @rpc(str, _returns=str)
    def authorization(ctx, jwtToVerify):
        if jwtToVerify in blacklist:
            ctx.transport.resp_status = 401
            blacklist.add(jwtToVerify)
            return 'Token invalid'
        response = authorize(jwtToVerify)
        if response is None:
            ctx.transport.resp_status = 401
            blacklist.add(jwtToVerify)
            return 'Token invalid'
        return response

    @rpc(str, _returns=str)
    def logout(ctx, jwtToVerify):
        blacklist.add(jwtToVerify)
        return 'Invalidat'

    @rpc(Integer, str, _returns=str)
    def get_user(ctx, id, token):
        validate = authorize(token)
        if validate is None:
            ctx.transport.resp_status = 401
            return 'Invalid token'

        [userID, roleID] = validate.split("-")
        userID = int(userID)
        roleID = int(roleID)
        if userID != id and roleID != 4:
            ctx.transport.resp_status = 403
            return 'Unathorized'
        print(f'Selecting user with id={id}')
        user = get_user(id)
        if user is None:
            return 'User not found'
        roles = ''
        for role in user.roles:
            roles += role.value + ", "
        return user.username + '-' + user.password + '-roles: ' + roles

    # @rpc(str, _returns=str)
    # def get_user_by_name(ctx, username):
    #     print(f'Selecting user with username={username}')
    #     user = get_user_by_name(username);
    #     if (user == None):
    #         return 'User not found'
    #     roles = ''
    #     for role in user.roles:
    #         roles += role.value + ", "
    #     return user.username + '-' + user.password + '-roles: ' + roles

    @rpc(str, _returns=str)
    def get_users(ctx, token):
        validate = authorize(token)
        if validate is None:
            ctx.transport.resp_status = 401
            return 'Invalid token'

        [userID, roleID] = validate.split("-")
        roleID = int(roleID)
        if roleID != 4:
            ctx.transport.resp_status = 403
            return 'Unathorized'
        print(f'Selecting all users')
        usersString = ''
        for user in get_users():
            roles = ''
            for role in user.roles:
                roles += str(role.id) + "*"
            usersString += f"{user.id} - {user.username} - {roles}\n"
        return usersString

    @rpc(Integer, str, str, str, _returns=bool)
    def update_user(ctx, id, username, password, token):
        validate = authorize(token)
        if validate is None:
            ctx.transport.resp_status = 401
            return 'Invalid token'
        [userID, roleID] = validate.split("-")
        userID = int(userID)
        roleID = int(roleID)
        if userID != id:
            ctx.transport.resp_status = 403
            return 'Unathorized'

        print(f"Updating user with id {id}")
        returnV = update_user(id, username, pwd_context.hash(password))
        return returnV

    @rpc(Integer, str, str, _returns=bool)
    def update_user_name(ctx, id, username, token):
        validate = authorize(token)
        if validate is None:
            ctx.transport.resp_status = 401
            return 'Invalid token'

        [userID, roleID] = validate.split("-")
        userID = int(userID)
        roleID = int(roleID)
        if userID != id:
            ctx.transport.resp_status = 403
            return 'Unathorized'
        print(f"Updating user username with id {id}")
        returnV = update_user_name(id, username)
        return returnV

    @rpc(Integer, str, str, _returns=bool)
    def update_user_password(ctx, id, password, token):
        validate = authorize(token)
        if validate is None:
            ctx.transport.resp_status = 401
            return 'Invalid token'
        [userID, roleID] = validate.split("-")
        userID = int(userID)

        if userID != id:
            ctx.transport.resp_status = 403
            return 'Unathorized'
        print(f"Updating user password with id {id}")
        returnV = update_user_pw(id, pwd_context.hash(password))
        return returnV

    @rpc(Integer, str, _returns=bool)
    def delete_user(ctx, id, token):
        validate = authorize(token)
        if validate is None:
            ctx.transport.resp_status = 401
            return 'Invalid token'

        [userID, roleID] = validate.split("-")
        userID = int(userID)
        roleID = int(roleID)
        if userID != id and roleID != 4:
            ctx.transport.resp_status = 403
            return 'Unathorized'
        print(f"Deleting user with id {id}")
        returnV = delete_user(id)
        return returnV

    @rpc(Integer, str, _returns=str)
    def get_role(ctx, id, token):
        validate = authorize(token)
        if validate is None:
            ctx.transport.resp_status = 401
            return 'Invalid token'

        [userID, roleID] = validate.split("-")
        roleID = int(roleID)
        if roleID != 4:
            ctx.transport.resp_status = 403
            return 'Unathorized'
        print(f"\n\nSelecting role with id {id}")
        role = get_role(id)
        if role is None:
            return 'Role not found'
        return get_role(id).value

    @rpc(str, _returns=str)
    def get_roles(ctx, token):
        validate = authorize(token)
        if validate is None:
            ctx.transport.resp_status = 401
            return 'Invalid token'

        [userID, roleID] = validate.split("-")
        roleID = int(roleID)
        if roleID != 4:
            ctx.transport.resp_status = 403
            return 'Unathorized'
        print("\n\nSelecting all roles")
        strRoles = ''
        for role in get_roles():
            strRoles += role.value + '\n'
        return strRoles

    @rpc(Integer, Integer, str, _returns=str)
    def add_role_to_user(ctx, userId, roleId, token):
        if(roleId==1):
            return str(create_user_role(userId, roleId))
        validate = authorize(token)
        if validate is None:
            ctx.transport.resp_status = 401
            return 'Unathorized'

        [userID, roleID] = validate.split("-")
        roleID = int(roleID)
        if roleID != 4:
            ctx.transport.resp_status = 403
            return 'Unathorized'
        print("Adding role to user")
        return str(create_user_role(userId, roleId))

    @rpc(Integer, Integer, str, _returns=bool)
    def delete_role_from_user(ctx, userId, roleId, token):
        validate = authorize(token)
        if validate is None:
            ctx.transport.resp_status = 401
            return 'Invalid token'

        [userID, roleID] = validate.split("-")
        roleID = int(roleID)
        if roleID != 4:
            ctx.transport.resp_status = 403
            return 'Unathorized'
        print("Removing role from user")
        return delete_user_role(userId, roleId)


application = Application([IDMService], 'services.identityManager.soap',
                          in_protocol=Soap11(validator='lxml'),
                          out_protocol=Soap11())

wsgi_application = WsgiApplication(application)


def cors_middleware(environ, start_response):
    def cors_start_response(status, headers, exc_info=None):
        if environ['REQUEST_METHOD'] == 'OPTIONS':
            start_response('200 OK', [
                ('Access-Control-Allow-Origin', '*'),
                ('Access-Control-Allow-Methods','*'),
                ('Access-Control-Allow-Headers', '*'),
                ("Access-Control-Allow-Credentials", "true")
            ])
            return []
        headers.append(('Access-Control-Allow-Origin', '*'))
        headers.append(('Access-Control-Allow-Methods',  '*'))
        headers.append(('Access-Control-Allow-Headers','*'))
        headers.append(("Access-Control-Allow-Credentials", "true"))
        return start_response(status, headers, exc_info)

    return wsgi_application(environ, cors_start_response)


if __name__ == '__main__':
    import logging

    from wsgiref.simple_server import make_server

    logging.basicConfig(level=logging.INFO)
    logging.getLogger('spyne.protocol.xml').setLevel(logging.INFO)

    logging.info("listening to http://localhost:8079")
    logging.info("wsdl is at: http://localhost:8079?wsdl")

    server = make_server('localhost', 8079, cors_middleware)
    server.serve_forever()
