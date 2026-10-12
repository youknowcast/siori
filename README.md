# siori（小説のしおり）

作品ごとの登場人物紹介と関係図を、オフラインで参照する Android アプリ。

## ビルド

```bash
mise trust
mise install
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

## 作品データの追加

1. `app/src/main/assets/catalog.json` の `works` に作品を追加する。
2. `app/src/main/assets/works/<workId>/characters.json` と `relations.mmd` を作成する。
   - 作品IDはディレクトリ名・`catalog.json.works[].id`・`characters.json.workId` で一致させる。
   - 作品ID（workId）の文字種は `[A-Za-z0-9_-]+` に限定する（ルート安全・アセットのディレクトリ名と一致）。
   - Mermaid の人物ノードIDは `characters.json` の人物IDに一致させる。補助ノードは `aux:` を付ける。
3. `./gradlew testDebugUnitTest` で整合性を検証する。

## テスト

```bash
./gradlew testDebugUnitTest
```
