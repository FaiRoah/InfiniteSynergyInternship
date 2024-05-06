build:
	mvn clean package -DskipTests
	docker compose build

run:
	docker compose up
