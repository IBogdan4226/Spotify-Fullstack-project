from base.sql_base import Session
from models.user_orm import User


def get_user(id):
    session = Session()
    user = session.query(User).get(id)
    if (user == None):
        print(f'User not found by {id}')
    return user;


def get_user_by_name(name):
    session = Session()
    user = session.query(User).filter(User.username == name).first();
    if (user == None):
        print(f'User not found by {name}')
    return user


def get_users():
    session = Session()
    users = session.query(User).all()
    return users


def create_user(username, password):
    session = Session()
    user = User(username, password)
    try:
        session.add(user)
        session.commit()
        return user.id;
    except Exception as exc:
        print(f"Failed to add user - {exc}")
        return False;


def update_user(id, username, password):
    session = Session()
    user = session.query(User).get(id)
    try:
        user.username = username
        user.password = password
        session.commit();
        return True
    except Exception as exc:
        print(f"Failed to update user - {exc}")
        return False


def update_user_name(id, username):
    session = Session()
    user = session.query(User).get(id)
    try:
        user.username = username
        session.commit()
        return True
    except Exception as exc:
        print(f"Failed to update user name- {exc}")
        return False


def update_user_pw(id, password):
    session = Session()
    user = session.query(User).get(id)
    try:
        user.password = password
        session.commit()
        return True
    except Exception as exc:
        print(f"Failed to update user password- {exc}")
        return False


def delete_user(id):
    session = Session()
    user = session.query(User).get(id)
    try:
        session.delete(user)
        session.commit()
        return True
    except Exception as exc:
        print(f"Failed to delete user - {exc}")
        return False


def verify_identity(username, password):
    user = get_user_by_name(username)
    if (user.password == password):
        print("User existent")
        return True
    else:
        print("User invalid")
        return False;
