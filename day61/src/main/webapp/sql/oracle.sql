select * from user_tables;

create table member(
	id varchar(20) primary key,
	password varchar(20),
	name varchar(30),
	role varchar(20)
);
insert into member values('test','1234','테스트','USER');
insert into member values('admin','1234','관리자','ADMIN');
select * from member;

create table board(
	bid int primary key,
	title varchar(50),
	writer varchar(30),
	content varchar(100),
	filename varchar(500)
);
insert into board(bid,title,writer,content) values(1,'제목 테스트','관리자','내용 테스트');
select * from board;