# Terraform stack (AWS) — MongoDB on EC2 (example)

This folder contains a minimal Terraform example that provisions a single EC2 instance running MongoDB inside Docker. It's intended for demo / dev environments only — for production consider managed MongoDB (Atlas) or a replica set with proper backups and monitoring.

Prerequisites
- AWS account and credentials configured (env vars or named profile).
- Terraform 1.0+

Quick start

```bash
cd terraform
terraform init
terraform plan -var 'region=us-east-1' -out plan.tf
terraform apply plan.tf
```

Security note: by default `allowed_cidr` may be set to 0.0.0.0/0 for demo convenience — change it to your office IP or VPN CIDR before applying.

After apply the output `mongo_public_ip` contains the address to connect (if you allowed external access). The instance will run MongoDB in Docker bound to port 27017.
