create database e12141198;

use e12141198;

CREATE TABLE badges_12141198 (id INT, class INT, dates CHAR(23), name
CHAR(23), tagbased BOOLEAN, userid INT) ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde' WITH SERDEPROPERTIES ("separatorChar" = ",", "quoteChar" = "\"");

LOAD DATA LOCAL INPATH '/home/adbs23/adbs23_shared/stackexchange/badges.csv' INTO
TABLE badges_12141198;

CREATE TABLE comments_12141198 (id INT, creationdate CHAR(23), idpost
INT, score INT, text CHAR(23), username CHAR(23), iduser INT)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde' WITH SERDEPROPERTIES ("separatorChar" = ",", "quoteChar" = "\"");

LOAD DATA LOCAL INPATH '/home/adbs23/adbs23_shared/stackexchange/comments.csv' INTO
TABLE comments_12141198;

CREATE TABLE posts_12141198 (id INT, idacceptedanswer INT, answercount
INT, body CHAR(23), closeddate CHAR(23), commentcount
INT, communityowneddate CHAR(23), creationdate CHAR(23), favoritecount
INT, lastactivitydate CHAR(23), lasteditdate
CHAR(23), lasteditordisplayname CHAR(23), lasteditoruserid
INT, ownerdisplayname CHAR(23), owneruserid INT, parentid INT, posttypeid
INT, score INT, tags CHAR(23), title CHAR(23), viewcount INT) ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde' WITH SERDEPROPERTIES ("separatorChar" = ",", "quoteChar" = "\"");

LOAD DATA LOCAL INPATH '/home/adbs23/adbs23_shared/stackexchange/posts.csv' INTO
TABLE posts_12141198;

CREATE TABLE postlinks_12141198 (id INT, creationdate CHAR(23),
linktypeid INT, postid INT, relatedpostid INT) ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde' WITH SERDEPROPERTIES ("separatorChar" = ",", "quoteChar" = "\"");

LOAD DATA LOCAL INPATH '/home/adbs23/adbs23_shared/stackexchange/postlinks.csv' INTO
TABLE postlinks_12141198;

CREATE TABLE users_12141198 (id INT, aboutme CHAR(23), accountid
INT, creationdate CHAR(23), displayname CHAR(23), downvotes
INT, lastaccessdate CHAR(23), location CHAR(23), profileimageurl
CHAR(23), reputation INT, upvotes INT, views INT, websiteurl CHAR(23)) ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde' WITH SERDEPROPERTIES ("separatorChar" = ",", "quoteChar" = "\"");

LOAD DATA LOCAL INPATH '/home/adbs23/adbs23_shared/stackexchange/users.csv' INTO
TABLE users_12141198;

CREATE TABLE votes_12141198 (id INT, bountyamount INT, creationdate
CHAR(23), postid INT, userid INT, votetypeid INT) ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde' WITH SERDEPROPERTIES ("separatorChar" = ",", "quoteChar" = "\"");

LOAD DATA LOCAL INPATH '/home/adbs23/adbs23_shared/stackexchange/votes.csv' INTO
TABLE votes_12141198;



select  u.id, count(*)
FROM users_12141198 u inner join posts_12141198 p on u.id = p.owneruserid
inner join comments_12141198 c on c.idpost = p.id
where c.score > 3 AND
EXISTS ( SELECT pl.postid,COUNT(*) as num
FROM postlinks_12141198 pl
WHERE pl.postid = p.id
GROUP BY pl.postid
HAVING num >= 10 AND num <= 20)
GROUP BY u.id;