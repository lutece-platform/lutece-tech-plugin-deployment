ALTER TABLE deployment_application DROP COLUMN code_category;
ALTER TABLE deployment_application DROP COLUMN site_name;
ALTER TABLE deployment_application ADD repo_type VARCHAR(100) DEFAULT NULL; 
ALTER TABLE deployment_application ADD artifact_id VARCHAR(100) DEFAULT NULL; 