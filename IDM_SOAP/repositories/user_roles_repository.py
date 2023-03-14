from sqlalchemy import and_

from base.sql_base import Session
from models.users_roles_orm import user_roles_relationship


def create_user_role(userID, roleID):
    session = Session()
    try:
        new_user_role = user_roles_relationship.insert().values(user_id=userID, role_id=roleID)
        session.execute(new_user_role)
        session.commit()
        return True
    except Exception as exc:
        print(f"Failed to add role with id {roleID} to user {userID} - {exc}")
        return False;


def delete_user_role(userID, roleID):
    session = Session()
    try:
        new_user_role = user_roles_relationship.delete().where(
            and_(user_roles_relationship.c.user_id == userID, user_roles_relationship.c.role_id == roleID))
        session.execute(new_user_role)
        session.commit()
        return True
    except Exception as exc:
        print(f"Failed to remove role with id {roleID} from user {userID} - {exc}")
        return False;
