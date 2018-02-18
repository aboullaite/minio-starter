### Minio spring boot starter
Spring boot starter project that autoconfigure a Minio client. 
[Minio](https://www.minio.io/) is a high performance distributed object storage server, designed for large-scale private cloud infrastructure. 

#### Usage
##### Start minio stack:
1. Add Docker swarm secrets
```
echo "AKIAIOSFODNN7EXAMPLE" | docker secret create access_key -
echo "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY" | docker secret create secret_key -
```
See []Minio Docker Quickstart Guide](https://docs.minio.io/docs/minio-docker-quickstart-guide)

2. Deploy stack
```
docker stack deploy --compose-file=minio-swarm.yml  minio_stack
```
3. Test proxy 
We configured Træfɪk with Host:minioproxy. So we need to have a dns record for `minioproxy` pointing to a ip address of any Docker swarm node or just add it to `/etc/hosts` for a quick test. 

#### Configuration
You can either use the rest API to interact with minio, by setting `minio.endpoint.enable = true` (default value is false), or using the `MinioTemplate` class service directly on your code. you need also to setup minion credentials.
Check the sample for a usage example