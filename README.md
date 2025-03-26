# Restaurant Chat App

A Kotlin Multiplatform chat application for restaurant employees, built with Compose Multiplatform and Supabase.

## Features

- User authentication
- General group chat for all employees
- 1:1 private chats between employees
- Real-time messaging
- Message history
- Online/offline status
- Modern Material 3 UI

## Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- Xcode 15.0 or newer (for iOS development)
- Kotlin 1.9.22
- JDK 17 or newer

## Setup

1. Clone the repository
2. Create a Supabase project and get your project URL and anon key
3. Update the `SupabaseClient.kt` file with your Supabase credentials:
   ```kotlin
   private const val SUPABASE_URL = "YOUR_SUPABASE_URL"
   private const val SUPABASE_KEY = "YOUR_SUPABASE_ANON_KEY"
   ```
4. Create the following tables in your Supabase database:

   ```sql
   -- Users table
   create table users (
     id uuid references auth.users on delete cascade,
     email text not null,
     name text not null,
     restaurant_id text not null,
     role text not null,
     avatar_url text,
     is_online boolean default false,
     last_seen bigint,
     primary key (id)
   );

   -- Channels table
   create table channels (
     id text primary key,
     type text not null,
     name text not null,
     participants text[] not null,
     last_message jsonb,
     created_at bigint not null,
     updated_at bigint not null,
     is_group boolean default false
   );

   -- Messages table
   create table messages (
     id text primary key,
     channel_id text references channels on delete cascade,
     sender_id text references users on delete cascade,
     content text not null,
     timestamp bigint not null,
     attachments jsonb default '[]',
     is_read boolean default false,
     is_edited boolean default false
   );
   ```

5. Build and run the project:
   - For Android: Open the project in Android Studio and click "Run"
   - For iOS: Open the Xcode project in the `iosApp` directory and click "Run"

## Project Structure

```
RestaurantChat/
├── shared/                 # Shared Kotlin code
│   ├── commonMain/        # Common code for all platforms
│   ├── androidMain/       # Android-specific code
│   └── iosMain/          # iOS-specific code
├── androidApp/            # Android application
└── iosApp/               # iOS application
```

## Dependencies

- Compose Multiplatform
- Supabase Kotlin
- Kotlin Coroutines
- Ktor
- Kotlin Serialization

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 