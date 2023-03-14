from datetime import datetime, timedelta

import jwt
from jwt import InvalidSignatureError

from repositories.user_repository import get_user

SECRET_KEY = "b832a0de6f4d6c37be25cf5173352aa82eb076fdb9f8c023a78be74f9a44f4e5"
ALGORITHM = "HS256"


def createJwt(user):
    roleIds = list((r.id for r in user.roles))
    if (len(roleIds) == 0):
        maxRole = 1
    else:
        maxRole = max(roleIds)
    encoded_jwt = jwt.encode({"iss": "http://127.0.0.1:8000",
                              "sub": user.id,
                              "exp": datetime.utcnow() + timedelta(minutes=30),
                              "jti": user.id,
                              "role": maxRole
                              }, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt


def authorize(jwtToVerify):
    try:
        payload = jwt.decode(jwtToVerify, SECRET_KEY, algorithms=[ALGORITHM])
        idUser = payload.get("sub")
        idRole = payload.get("role")
        user = get_user(idUser);
        if (user == None):
            return None
        roles = []
        for role in user.roles:
            roles.append(role.id)
        if idRole not in roles:
            return None
        return f'{idUser}-{idRole}'
    except jwt.ExpiredSignatureError:
        return None
    except InvalidSignatureError:
        return None
    except Exception:
        return None
