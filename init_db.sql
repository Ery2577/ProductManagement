postgres=# create database product_management_db;
CREATE DATABASE

postgres=# create user product_manager_user password '123456';
CREATE ROLE

postgres=# \c product_management_db
You are now connected to database "product_management_db" as user "postgres".

product_management_db=# grant create on schema public to product_manager_user;
GRANT

product_management_db=#

