use dbproject221;

insert into categories
value(1,'ทั่วไป');

insert into categories
value(2,'ทุนการศึกษา');

insert into categories
value(3,'หางาน');

insert into categories
value(4,'ฝึกงาน');

insert into announces
values (1,'บริษัท เน็ตเซอร์พลัส จำกัด รับสมัครงาน 2 ตำแหน่ง','บริษัท เน็ตเซอร์พลัส จำกัด เปิดรับสมัครงาน 2 ตำแหน่ง Application Support และ Customer Support',NULL,NULL,'N',3);

insert into announces
values (2,'รายชื่อนักศึกษาที่ได้รับทุนการศึกษาประเภท "ทุนจ้างงาน" 2/2565','คณะ ฯ ประกาศรายชื่อนักศึกษาที่ได้รับทุนการศึกษาประเภท "ทุนจ้างงาน" ประจำภาคการศึกษา 2/2565',NULL,'2023-05-31 18:00:00+07:00','Y',2);

insert into announces
values (3,'แนวปฎิบัติการสอบออนไลน์ พ.ศ. 2565','ประกาศมหาวิทยาลัยเทคโนโลยีพระจอมเกล้าธนบุรี เรื่องแนวทางปฎิบัติการสอบออนไลน์ พ.ศ. 2565','2023-01-27 06:00:00+07:00 ',NULL,'Y',1);

insert into announces
values (4, 'กิจกรรมพี่อ้อย พี่ฉอด On Tour 2566','ขอเชิญนักศึกษาทุกชั้นปี เข้าร่วมกิจกรรมพี่อ้อย พี่ฉอด On Tour', '2023-04-19 06:00:00+07:00','2023-05-08 18:00:00+07:00','Y',1);

-- insert into users (username,name, email, role, createdOn,updatedOn) values 
--     ('sanit','Sanit Sirisawatvatana','sanit.sir@kmutt.ac.th','admin','2023-08-15 08:00:00+07:00','2023-08-15 08:00:00+07:00'),
--     ('pornthip','Pornthip Sirijutikul','pornthip.sri@kmutt.ac.th','announcer','2023-08-15 09:30:00+07:00','2023-08-15 09:30:00+07:00'),
--     ('jaruwan_w','Jaruwan Maneesart','jaruwan.wee@kmutt.ac.th','announcer','2023-08-16 08:00:00+07:00','2023-08-16 08:00:00+07:00'),
--     ('vichchuda','Vichchuda Tedoloh','vichchuda.ted@kmutt.ac.th','announcer','2023-08-16 09:30:00+07:00','2023-08-16 09:30:00+07:00');
    
--  insert into users (username,name, email, role,password) values
--     ("sanit","Sanit Sirisawatvatana","sanit.sir@kmutt.ac.th","admin","Sasadmin22"),
-- 	("pornthip","Pornthip Sirijutikul","pornthip.sri@kmutt.ac.th","announcer","Sasbscit22"),
-- 	("jaruwan_w","Jaruwan Maneesart","jaruwan.wee@kmutt.ac.th","announcer","Sasinter22"),
--     ("vichchuda","Vichchuda Tedoloh","vichchuda.ted@kmutt.ac.th","announcer","Sasgrant22");

    insert into users (username,name, email, role,password) values
    ("sanit","Sanit Sirisawatvatana","sanit.sir@kmutt.ac.th","admin","$argon2id$v=19$m=4096,t=3,p=1$L3JqldzNPgJSXBP32BHE8g$PkhU++96ArYk9zx/yIueaJQiMAsvMrtsOGLT+9ykop8"),
	("pornthip","Pornthip Sirijutikul","pornthip.sri@kmutt.ac.th","announcer","$argon2id$v=19$m=4096,t=3,p=1$OjVWHuq9UBoq3U40YTRryw$SDvqOZRsCKuXddVn4Zl11f5j/jwPpntATMfkLI7YYMY"),
	("jaruwan_w","Jaruwan Maneesart","jaruwan.wee@kmutt.ac.th","announcer","$argon2id$v=19$m=4096,t=3,p=1$fw1K6OWEP4NfHzAFxBr3jw$j44BwAEAt9FzesDFcPZ0lrIz/K/DmEvI+K4LW5VWigA"),
    ("vichchuda","Vichchuda Tedoloh","vichchuda.ted@kmutt.ac.th","announcer","$argon2id$v=19$m=4096,t=3,p=1$tApX5Lho109uf4Jr3qBjXQ$fsK+WJIATgvZfQHcRoLD+eopu1eGDXcEly+gMJCqBj4"); 