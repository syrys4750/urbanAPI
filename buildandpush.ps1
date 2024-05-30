docker login

# Define an array with the directories where you need to run the command
$directories = @(
  "AGGREGATED_NOSQL_REPOSITORY",
  "AUTH_API",
  "MOBILITY_API",
  "MOBILITY_NOSQL_REPOSITORY",
  "MOBILITY_SQL_REPOSITORY",
  "MUNICIPAL_API",
  "POLLUTION_API",
  "POLLUTION_NOSQL_REPOSITORY",
  "POLLUTION_SQL_REPOSITORY"
)

# Store the current location
$originalPath = Get-Location

# Loop through each directory and run the mvn clean package command
foreach ($dir in $directories) {
  $lowercaseDir = $dir.ToLower()
  Write-Host "Running mvn clean package in $dir"
  Set-Location -Path "$originalPath\$dir"
  mvn clean package

  Write-Host "Pushing Docker image for $lowercaseDir"
  docker build -t "sersomon/$($lowercaseDir)" .
  docker push "sersomon/$($lowercaseDir):latest"
}

# Return to the original path
Set-Location -Path $originalPath

