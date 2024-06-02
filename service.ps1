# Set the interval in seconds
$interval = $env:REQUEST_INTERVAL
if (-not $interval) {
    # Default interval of 900 seconds (15 minutes) if the environment variable is not set
    $interval = 900
}
else {
    # Convert to integer if it's a string
    $interval = [int]$interval
}

# URL to send GET requests
$url = "http://localhost:8087/api/v1/aggregateData"

# JWT token
$token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZSI6IlJPTEVfU0VSVklDRSIsImlzcyI6InNlcnNvbW9uIiwiZXhwIjoxNzQ4ODYzOTY2fQ.IhwP-DYX4G3hUIRD-T8_QkaXstdVYYF-pF63BKE12bE"

# Function to send GET request using httpie
function Send-GetRequest {
    try {
        $response = http --ignore-stdin GET $url -A bearer -a $token
        Write-Output "Response received at $(Get-Date): $response"
    }
    catch {
        Write-Error "Error sending GET request: $_"
    }
}

# Infinite loop to send requests every X seconds
while ($true) {
    Send-GetRequest
    Start-Sleep -Seconds $interval
}
