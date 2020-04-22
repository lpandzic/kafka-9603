# kafka-9603

Small project replicating [the issue with same name](https://issues.apache.org/jira/browse/KAFKA-9603).

Requirements:
* docker (6 GB RAM dedicated to docker)
* java 11
* maven

Steps to reproduce:

`mvn clean install`

`export DOCKER_HOST_IP=<local.ip>`

`docker-compose up`

[Open grafana](http://localhost:3000/d/Zb54iIqZk/open-files?panelId=2&fullscreen&orgId=1&refresh=10s). (admin:foobar)

On master branch - after ~ 5-10 minutes open files count should steadily increase every minute.

On branch spring-boot-2.1.5 open files count is stable.