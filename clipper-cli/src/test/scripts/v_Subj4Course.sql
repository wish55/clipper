DROP VIEW v_Subj4Course CASCADE;
CREATE OR REPLACE VIEW v_Subj4Course AS 
SELECT innerRel.x1 AS att1
FROM (
SELECT ca_0.individual AS x1
FROM  concept_assertion ca_0
WHERE ca_0.concept=44
) as innerRel
