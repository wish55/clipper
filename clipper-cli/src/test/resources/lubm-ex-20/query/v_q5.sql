CREATE OR REPLACE VIEW v_q5 AS 
(
SELECT 
v_Publication_1.att1 AS att1 
FROM 
v_Student v_Student_1,
v_Professor v_Professor_1,
v_Publication v_Publication_1,
v_publicationAuthor v_publicationAuthor_1,
v_publicationAuthor v_publicationAuthor_2
WHERE 
v_publicationAuthor_1.att2 = v_Professor_1.att1
 AND v_Publication_1.att1 = v_publicationAuthor_2.att1
 AND v_Publication_1.att1 = v_publicationAuthor_1.att1
 AND v_publicationAuthor_1.att2 = v_Student_1.att1
)
