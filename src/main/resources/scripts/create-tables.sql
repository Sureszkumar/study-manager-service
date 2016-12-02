
  CREATE
  TABLE USER
  (
    ID                    INTEGER NOT NULL AUTO_INCREMENT ,
    CREATION_DATE_TIME    TIMESTAMP NOT NULL,
    LAST_CHANGE_TIMESTAMP TIMESTAMP NOT NULL,
    EMAIL                 VARCHAR(35) NOT NULL,
    PASSWORD              VARCHAR(255) NOT NULL,
    AUTH_TOKEN            VARCHAR(255),
    VERIFIED      		  BOOLEAN,
    EMAIL_VERIFY_TOKEN 	  VARCHAR(255),
    PRIMARY KEY (ID)
  );
  
  
  CREATE
  TABLE COURSE
  (
    ID                    INTEGER NOT NULL AUTO_INCREMENT ,
    CREATION_DATE_TIME    TIMESTAMP NOT NULL,
    LAST_CHANGE_TIMESTAMP TIMESTAMP NOT NULL,
    TITLE                 VARCHAR(255) NOT NULL,
    DESCRIPTION           VARCHAR(255),
    TYPE                  VARCHAR(35),
    DEFAULT_TIME_IN_WEEKS INTEGER,
    PRIMARY KEY (ID)
  );
  
  CREATE
  TABLE BOOK
  (
    ID                    INTEGER NOT NULL AUTO_INCREMENT ,
    CREATION_DATE_TIME    TIMESTAMP NOT NULL,
    LAST_CHANGE_TIMESTAMP TIMESTAMP NOT NULL,
    TITLE                 VARCHAR(255) NOT NULL,
    DESCRIPTION           VARCHAR(255),
    NO_OF_PAGES			  INTEGER,
    TYPE                  VARCHAR(35),
    ISBN			  	  VARCHAR(35),
    PRIMARY KEY (ID)
  );
  
  CREATE 
  TABLE COURSE_BOOKS 
  (
	ID                		INTEGER NOT NULL AUTO_INCREMENT ,
	CREATION_DATE_TIME   	TIMESTAMP NOT NULL,
    LAST_CHANGE_TIMESTAMP 	TIMESTAMP NOT NULL,
	COURSE_ID              	INTEGER NOT NULL,
	BOOK_ID              	INTEGER NOT NULL,
  	PRIMARY KEY (ID)
  );

  
  CREATE 
  TABLE WEEKLY_HOURS 
  (
 	ID                		INTEGER NOT NULL AUTO_INCREMENT ,
 	CREATION_DATE_TIME   	TIMESTAMP NOT NULL,
    LAST_CHANGE_TIMESTAMP 	TIMESTAMP NOT NULL,
    MONDAY                		INTEGER,
    TUESDAY  	          		INTEGER,
    WEDNESDAY              		INTEGER,
    THURSDAY               		INTEGER,
    FRIDAY                		INTEGER,
    SATURDAY               		INTEGER,
    SUNDAY               		INTEGER
   PRIMARY KEY (ID)
  ) ;
  
  CREATE 
  TABLE WEEKLY_PAGES 
  (
 	ID                		INTEGER NOT NULL AUTO_INCREMENT ,
 	CREATION_DATE_TIME   	TIMESTAMP NOT NULL,
    LAST_CHANGE_TIMESTAMP 	TIMESTAMP NOT NULL,
    MONDAY                		INTEGER,
    TUESDAY  	          		INTEGER,
    WEDNESDAY              		INTEGER,
    THURSDAY               		INTEGER,
    FRIDAY                		INTEGER,
    SATURDAY               		INTEGER,
    SUNDAY               		INTEGER
   PRIMARY KEY (ID)
  ) ;

  
  CREATE 
  TABLE USER_COURSES 
  (
	ID                		INTEGER NOT NULL AUTO_INCREMENT ,
	CREATION_DATE_TIME   	TIMESTAMP NOT NULL,
    LAST_CHANGE_TIMESTAMP 	TIMESTAMP NOT NULL,
	USER_ID                	INTEGER NOT NULL,
	COURSE_ID              	INTEGER NOT NULL,
	START_DATE				DATE,
	END_DATE				DATE,
	PROFICIENCY				VARCHAR(35),
	CURRENT_STATUS			VARCHAR(35),
	COMPLETION_RATE			INTEGER,
	 WEEKLY_PAGES_ID 		INTEGER,
    WEEKLY_HOURS_ID			INTEGER,
  	PRIMARY KEY (ID),
  	KEY fk_user_courses_weekly_pages (weekly_pages_id),
  	KEY fk_user_courses_weekly_hours (weekly_hours_id),
  	CONSTRAINT fk_user_courses_weekly_pages FOREIGN KEY (weekly_pages_id) REFERENCES WEEKLY_PAGES (ID) ON DELETE CASCADE ON UPDATE CASCADE,
  	CONSTRAINT fk_user_courses_weekly_hours FOREIGN KEY (weekly_hours_id) REFERENCES WEEKLY_HOURS (ID) ON DELETE CASCADE ON UPDATE CASCADE
  ) ;