DROP TABLE IF EXISTS PRICES;

CREATE TABLE PRICES (
                        BRAND_ID INT,
                        START_DATE TIMESTAMP,
                        END_DATE TIMESTAMP,
                        PRICE_LIST INT,
                        PRODUCT_ID INT,
                        PRIORITY INT,
                        PRICE DECIMAL(10, 2),
                        CURR VARCHAR(3),
                        PRIMARY KEY (BRAND_ID, PRODUCT_ID, START_DATE)
);

CREATE INDEX IDX_PRICES_DATES ON PRICES(BRAND_ID, PRODUCT_ID, START_DATE, END_DATE);
