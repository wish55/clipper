DROP VIEW v_telephone CASCADE;
CREATE OR REPLACE VIEW v_telephone AS 
SELECT x1 AS att0, x2 AS att1
FROM (
SELECT dra_0.value AS x1, dra_0.individual AS x2
FROM  data_role_assertion dra_0
WHERE dra_0.data_role=174
) as innerRel
