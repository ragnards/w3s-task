FROM maven
WORKDIR /tests
COPY . .
RUN mvn dependency:go-offline
CMD ["mvn", "test", "allure:serve"]
