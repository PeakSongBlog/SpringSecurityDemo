pluginManagement {
    repositories {
        mavenLocal()
        maven{ setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        maven{ setUrl("https://maven.aliyun.com/repository/spring-plugin") }
        maven{ setUrl("https://maven.aliyun.com/repository/central") }
        maven{ setUrl("https://maven.aliyun.com/repository/public") }
    }
}

rootProject.name = "juejin1"
