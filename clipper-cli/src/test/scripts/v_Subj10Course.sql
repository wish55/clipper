DROP VIEW v_Subj10Course CASCADE;
CREATE OR REPLACE VIEW v_Subj10Course AS 
SELECT innerRel.x1 AS att1
FROM (
SELECT ca_0.individual AS x1
FROM  concept_assertion ca_0
WHERE ca_0.concept=3
) as innerRel
