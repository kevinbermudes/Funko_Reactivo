plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Lombook para generar código, poner todo esto para que funcione
    implementation("org.projectlombok:lombok:1.18.28")
    testImplementation("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    // Para nuestros test
    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    // Mockito para nuestros test con JUnit 5
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")
    testImplementation("org.mockito:mockito-core:5.5.0")

    // HikaryCP para la conexión a la BD
    implementation("com.zaxxer:HikariCP:5.0.1")
    // Logger
    implementation("ch.qos.logback:logback-classic:1.4.11")
    // Ibatis lo usaremos para leer los scripts SQL desde archivos
    implementation("org.mybatis:mybatis:3.5.13")
    // gson
    implementation("com.google.code.gson:gson:2.8.8")
    // R2DBC
    implementation("io.r2dbc:r2dbc-h2:1.0.0.RELEASE")
    implementation("io.r2dbc:r2dbc-pool:1.0.0.RELEASE")
// Project Reactor
    implementation("io.projectreactor:reactor-core:3.5.10")
    // Para test: https://www.baeldung.com/reactive-streams-step-verifier-test-publisher
    // NO lo vamos a usar, pero lo dejo por si acaso
    // testImplementation("io.projectreactor:reactor-test:3.5.10")

    // Logger
    implementation("ch.qos.logback:logback-classic:1.4.11")
    //reactor tests
    testImplementation("io.projectreactor:reactor-test:3.5.10")

}

tasks.test {
    useJUnitPlatform()
}