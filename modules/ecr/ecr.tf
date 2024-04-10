resource "aws_ecr_repository" "ecr_repo" {
  name                 = var.repository_name
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}
resource "aws_ecr_lifecycle_policy" "ecr_policy" {
  repository = aws_ecr_repository.ecr_repo.name
  policy     = <<EOF
{
  "rules": [
    {
      "rulePriority": 1,
      "description": "Keep last 1 images",
      "selection": {
        "tagStatus": "tagged",
        "tagPrefixList": ["snapshot"],
        "countType": "imageCountMoreThan",
        "countNumber": 1
      },
      "action": {
        "type": "expire"
      }
    }
  ]
}
EOF
}
