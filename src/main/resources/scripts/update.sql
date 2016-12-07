set @i:=0;
update user_courses set priority=@i where user_id = 1 and (@i:=@i+1);

UPDATE user_courses, course
SET    user_courses.description = course.description,
user_courses.title = course.title,
user_courses.type = course.type
WHERE  user_courses.course_id = course.id;