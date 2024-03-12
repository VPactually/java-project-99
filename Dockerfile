FROM gradle:8.6.0-jdk20

WORKDIR /

COPY / .

RUN ./gradlew installDist

CMD ./build/install/app/bin/app