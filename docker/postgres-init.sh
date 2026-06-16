#!/bin/sh
set -eu

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" -f /docker-entrypoint-initdb.d/sql/01_create_database.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname loginapp_db -f /docker-entrypoint-initdb.d/sql/02_schema.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname loginapp_db -f /docker-entrypoint-initdb.d/sql/03_functions.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname loginapp_db -f /docker-entrypoint-initdb.d/sql/04_seed_data.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname loginapp_db -f /docker-entrypoint-initdb.d/sql/05_test_queries.sql
