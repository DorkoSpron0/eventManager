```toml
name = 'RegisterUser'
method = 'POST'
url = 'http://localhost:8787/iasapi/users'
sortWeight = 1000000
id = '0d8cb5d3-0851-488e-8ed8-2748ccab4890'

[[headers]]
key = 'Content-Type'
value = 'application/json'

[body]
type = 'JSON'
raw = '''
{
  "username": "NickyFF",
  "password": "Hello1"
}'''
```
