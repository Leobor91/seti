data "aws_ami" "amazon_linux2" {
  most_recent = true
  owners      = ["amazon"]
  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }
}

resource "aws_security_group" "mongo_sg" {
  name        = "franchise-mongo-sg"
  description = "Allow MongoDB and SSH"

  ingress {
    description = "MongoDB"
    from_port   = 27017
    to_port     = 27017
    protocol    = "tcp"
    cidr_blocks = [var.allowed_cidr]
  }

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.allowed_cidr]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "mongo" {
  ami                    = data.aws_ami.amazon_linux2.id
  instance_type          = var.instance_type
  vpc_security_group_ids = [aws_security_group.mongo_sg.id]

  # optional key pair
  key_name = var.ssh_key_name != "" ? var.ssh_key_name : null

  user_data = <<-EOF
              #!/bin/bash
              yum update -y
              amazon-linux-extras install docker -y || yum install -y docker
              service docker start
              usermod -a -G docker ec2-user || true
              docker run -d --name mongo -p 27017:27017 -v /var/lib/mongo:/data/db mongo:6.0 --bind_ip_all
              EOF

  tags = {
    Name = "franchise-mongo"
  }
}
