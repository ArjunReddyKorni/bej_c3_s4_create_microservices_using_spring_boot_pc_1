FROM openjdk
WORKDIR usr/lib
ENV MONGO_DATABASE="productUserdb"
ENV MONGO_URL="mongodb://localhost:27017/productUserdb"

ADD ./target/UserProduct-0.0.1-SNAPSHOT.jar /usr/lib/UserProduct-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","UserProduct-0.0.1-SNAPSHOT.jar"]