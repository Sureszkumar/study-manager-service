--insert data for book

insert into book (id, title, description, no_Of_Pages, author, isbn, type) values
(1, "Pmp Exam Prep", "PMP® Exam Practice Test and Study Guide, Ninth Edition", 
544, "Rita Mulcahy", "1482202247", "RECOMMENDED");

insert into book (id, title, description, no_Of_Pages, author, isbn, type) values
(2, "Head First PMP", "Head First PMP 3rd Edition", 
834 , "Jennifer Greene", "9781449364915", "RECOMMENDED");

--insert data for course

insert into course (id, title, description,type,defaultTimeInWeeks) values
(1, "PMP certification", "Project Management Professional Certification","RECOMMENDED", 11);

--insert data to link course and book

insert into COURSE_BOOKS(id, course_id, user_id) values(1,1,1);
insert into COURSE_BOOKS(id, course_id, user_id) values(1,1,2);

--insert data for course proficiency
insert into COURSE_PROFICIENCY(id, course_id, easy_pages, normal_pages, difficult_pages)
values(1, 1, 18, 15, 10);


