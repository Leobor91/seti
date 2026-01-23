variable "region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "instance_type" {
  description = "EC2 instance type for MongoDB"
  type        = string
  default     = "t3.micro"
}

variable "ssh_key_name" {
  description = "(optional) existing EC2 key pair name to enable SSH"
  type        = string
  default     = ""
}

variable "allowed_cidr" {
  description = "CIDR allowed to access MongoDB (27017). Use a restricted CIDR for safety."
  type        = string
  default     = "0.0.0.0/0"
}
