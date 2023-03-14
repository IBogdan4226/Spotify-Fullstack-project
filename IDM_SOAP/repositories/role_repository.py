from base.sql_base import Session
from models.role_orm import Role


def get_roles():
    session = Session()
    roles = session.query(Role).all()
    return roles


def get_role(id):
    session = Session()
    role = session.query(Role).get(id)
    if (role == None):
        print(f'Role not found by {id}')
    return role;
