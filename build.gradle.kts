plugins {
	java
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.valebroker"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.apache.commons:commons-crypto:1.1.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")



}

tasks.withType<Test> {
	useJUnitPlatform()
}
