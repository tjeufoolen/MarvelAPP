# MarvelAPP
An android app written in Kotlin which uses the Marvel API to demonstrate service usage.

## Getting Started
1. Clone this project
2. Open the project using Android Studio
3. Create a resource file named `api_credentials.yml` under `app/res/values`.
4. Copy the template from below and fill in the credentials.
5. Run the app
6. Enjoy! :tada:

### Template (api_credentials.yml)
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="api_base_url">https://gateway.marvel.com:443/v1/public</string>

    <!-- Get keys at https://developer.marvel.com/ -->
    <string name="api_public_key"></string>
    <string name="api_private_key"></string>
</resources>
```

## Authors
- [Micha Nijenhof](https://github.com/nijenhof)
- [Tjeu Foolen](https://github.com/tjeufoolen)
