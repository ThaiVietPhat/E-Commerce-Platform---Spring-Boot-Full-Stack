ALTER TABLE category
    ADD COLUMN version INT DEFAULT 0,
    ADD COLUMN created_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    ADD COLUMN last_modified_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6);

-- Nếu bạn muốn chắc chắn rằng dữ liệu cũ (nếu có) cũng có giá trị ban đầu:
-- UPDATE category SET version = 0, created_date = NOW(6), last_modified_date = NOW(6) WHERE created_date IS NULL;