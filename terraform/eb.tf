resource "aws_vpc" "pay_vpc" {
  cidr_block = "172.30.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true

  tags = {
    Name = "iaw-clo-ss-vpc-an2-dev-1"
  }
}

resource "aws_internet_gateway" "pay_igw" {
  vpc_id = aws_vpc.pay_vpc.id

  tags = {
    Name = "iaw-clo-ss-igw-an2-dev-1"
  }
}

resource "aws_route" "pay_route" {
  route_table_id         = aws_vpc.pay_vpc.main_route_table_id
  destination_cidr_block = "0.0.0.0/0"  # Default route
  gateway_id             = aws_internet_gateway.pay_igw.id
}

resource "aws_subnet" "public_subnet_1" {
  vpc_id                  = aws_vpc.pay_vpc.id
  cidr_block              = "172.30.1.0/24"
  availability_zone = "ap-northeast-2a"

  tags = {
    Name = "iaw-clo-ss-public_subnet-an2-dev-1"
  }
}

resource "aws_subnet" "public_subnet_2" {
  vpc_id                  = aws_vpc.pay_vpc.id
  cidr_block              = "172.30.2.0/24"
  availability_zone = "ap-northeast-2b"

  tags = {
    Name = "iaw-clo-ss-public_subnet-an2-dev-1"
  }
}

resource "aws_ecr_repository" "ecr" {
  name                 = "pay"
}

resource "aws_elastic_beanstalk_application" "my_app" {
  name = "iaw-clo-ss-eb_pay-an2-dev-1"
}

resource "aws_elastic_beanstalk_environment" "pay" {
  name                = "iaw-clo-ss-eb-pay-an2-dev-1"
  application         = aws_elastic_beanstalk_application.my_app.name
  solution_stack_name = "64bit Amazon Linux 2023 v4.2.2 running Docker"
  tier                = "WebServer"

  setting {
    namespace = "aws:ec2:vpc"
    name      = "VPCId"
    value     = aws_vpc.pay_vpc.id
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "Subnets"
    value     = join(",", [aws_subnet.public_subnet_1.id, aws_subnet.public_subnet_2.id])
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "AssociatePublicIpAddress"
    value     =  "True"
  }

  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MinSize"
    value     = "1"
  }

  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MaxSize"
    value     = "2"
  }

  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "EnvironmentType"
    value     = "LoadBalanced"
  }

  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "LoadBalancerType"
    value     = "application"
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "InstanceType"
    value     = "t3.micro"
  }

  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "ServiceRole"
    value     = "aws-elasticbeanstalk-service-role"
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = "aws-elasticbeanstalk-ec2-role"
  }

  setting {
    namespace = "aws:elasticbeanstalk:application"
    name      = "Application Healthcheck URL"
    value     = "/"
  }

  setting {
    namespace = "aws:elasticbeanstalk:healthreporting:system"
    name      = "SystemType"
    value     = "enhanced"
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "ELBScheme"
    value     = "internet facing"
  }

  setting {
    namespace = "aws:elasticbeanstalk:environment:process:default"
    name      = "MatcherHTTPCode"
    value     = "200"
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "EC2KeyName"
    value     = "younghokwak"
  }

  setting {
    namespace = "aws:elasticbeanstalk:environment:process:default"
    name      = "Port"
    value     = "8080"
  }

  setting {
    name = "Unit"
    namespace = "aws:autoscaling:trigger"
    value = "Percent"
  }
  setting {
    name = "MeasureName"
    namespace = "aws:autoscaling:trigger"
    value = "CPUUtilization"
  }
  setting {
    name = "LowerThreshold"
    namespace = "aws:autoscaling:trigger"
    value = "20"
  }
  setting {
    name = "UpperThreshold"
    namespace = "aws:autoscaling:trigger"
    value = "90"
  }
  setting {
    name = "Period"
    namespace = "aws:autoscaling:trigger"
    value = "5"
  }
  setting {
    name = "UpperBreachScaleIncrement"
    namespace = "aws:autoscaling:trigger"
    value = "1"
  }
  setting {
    name = "LowerBreachScaleIncrement"
    namespace = "aws:autoscaling:trigger"
    value = "-1"
  }

}