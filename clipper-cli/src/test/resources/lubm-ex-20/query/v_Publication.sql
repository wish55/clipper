CREATE OR REPLACE VIEW v_Publication AS 
SELECT name_0.id AS att1, name_0.name AS name
FROM (
SELECT ca_0.individual AS x1
FROM  concept_assertion ca_0
WHERE ca_0.concept=97
UNION 
SELECT ca_0.individual AS x1
FROM  concept_assertion ca_0
WHERE ca_0.concept=75
UNION 
SELECT ora_0.a AS x1
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=142
UNION 
SELECT ca_0.individual AS x1
FROM  concept_assertion ca_0
WHERE ca_0.concept=46
UNION 
SELECT ca_0.individual AS x1
FROM  concept_assertion ca_0
WHERE ca_0.concept=96
UNION 
SELECT ora_0.b AS x1
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=156
UNION 
SELECT ca_0.individual AS x1
FROM  concept_assertion ca_0
WHERE ca_0.concept=49
UNION 
SELECT ca_0.individual AS x1
FROM  concept_assertion ca_0
WHERE ca_0.concept=14
UNION 
SELECT ora_0.a AS x1
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=162
UNION 
SELECT ca_0.individual AS x1
FROM  concept_assertion ca_0
WHERE ca_0.concept=1
UNION 
SELECT ora_0.a AS x1
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=156
UNION 
SELECT ca_0.individual AS x1
FROM  concept_assertion ca_0
WHERE ca_0.concept=5
UNION 
SELECT ca_0.individual AS x1
FROM  concept_assertion ca_0
WHERE ca_0.concept=111
UNION 
SELECT ca_0.individual AS x1
FROM  concept_assertion ca_0
WHERE ca_0.concept=79
UNION 
SELECT ora_0.a AS x1
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=128
UNION 
SELECT ora_0.a AS x1
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=139
UNION 
SELECT ora_0.b AS x1
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=148
) as innerRel , individual_name name_0
WHERE  innerRel.x1=name_0.id 