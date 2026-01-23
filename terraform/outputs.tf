output "mongo_public_ip" {
  description = "Public IP of the EC2 instance running MongoDB"
  value       = aws_instance.mongo.public_ip
}

output "mongo_security_group_id" {
  description = "Security Group id for MongoDB instance"
  value       = aws_security_group.mongo_sg.id
}
