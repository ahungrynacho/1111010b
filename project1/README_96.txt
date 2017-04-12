AWS Public IPv4 54.213.222.159

TOMCAT ON AWS
http://54.213.222.159:8080
tomcat username: bbhuynh
tomcat password: spring2017

TOMCAST ON LOCALHOST
C:\apache-tomcat-8.5.13\bin\startup.bat
http://localhost:8080
tomcat username: bbhuynh
tomcat password: spring2017

SQL LOGIN
mysql -u root -p
mySQL username: bbhuynh
mySQL password: spring2017
mysql> create database moviedb;
mysql> moviedb < createtable.sql

SSH AWS ALIAS
ssh aws
~/.ssh/conf
Host aws
	HostName ec2-54-213-222-159.us-west-2.compute-amazonaws.com
	Port 22
	User ubuntu
	IdentityFile ~/.ssh/fabflix.pem	

JDBC TEST CASES
Insert Star A (first name, last name)
id: 0
first_name: brian
last_name: huynh
dob: 1996/05/28
photo_url:

Insert Star B (last name only)
id: 1
last_name: lee
dob:
photo_url:

Insert Customer
id: 2
first_name: daniel
last_name: nguyen
cc_id: 960
address: uci
email: dn@uci.edu
password: spring2017

Search Star (first name)
first_name: tom

Search Star (last name)
last_name: hanks

Search Star (first and last name)
first_name: tom
last_name: hanks

Search Star (id)
id: 788010

Delete One Customer (id)
id: 135001

Metadata
$ 5

SQL Queries
$ 0
$ update customers set last_name = 'xyz' where first_name = 'john'
10 record(s) successfully updated.