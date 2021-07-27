#
# mvn spring-boot:build-image
#

FROM openjdk

ENV SIMULATOR=/opt/market-simulator
COPY ./ ${SIMULATOR}/

ENV MVN_VERSION=3.8.1
ENV MVN=apache-maven-${MVN_VERSION}

ENV PATH=/opt/maven/bin:$PATH
RUN cd /tmp && curl -O http://mirrors.advancedhosters.com/apache/maven/maven-3/${MVN_VERSION}/binaries/${MVN}-bin.tar.gz \
	&& (cd /opt/ ; tar xzf /tmp/${MVN}-bin.tar.gz) && \
	(mv /opt/${MVN} /opt/maven)

WORKDIR ${SIMULATOR}

RUN mvn clean install

ENTRYPOINT ["java", "-jar", "target/market-simulator.jar"]