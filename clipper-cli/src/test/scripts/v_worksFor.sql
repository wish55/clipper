DROP VIEW v_worksFor CASCADE;
CREATE OR REPLACE VIEW v_worksFor AS 
SELECT innerRel.x2 AS att1, innerRel.x1 AS att2
FROM (
SELECT ora_0.b AS x1, ora_0.a AS x2
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=164
UNION 
SELECT ora_0.b AS x1, ora_0.a AS x2
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=151
UNION 
SELECT ora_0.b AS x1, ora_0.a AS x2
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=160
UNION 
SELECT ora_0.b AS x1, ora_0.a AS x2
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=143
UNION 
SELECT ora_0.b AS x1, ora_0.a AS x2
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=170
UNION 
SELECT ora_0.a AS x1, ora_0.b AS x2
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=129
UNION 
SELECT ora_0.b AS x1, ora_0.a AS x2
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=136
UNION 
SELECT ora_0.b AS x1, ora_0.a AS x2
FROM  object_role_assertion ora_0
WHERE ora_0.object_role=144
) as innerRel
