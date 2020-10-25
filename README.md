# プレゼンプラグイン

## 説明
astah*のモデルの表示状態を記録して再生できるプラグインです。
モデルを説明するときの助けになるツールです。

# ダウンロード
- [ここ](https://github.com/snytng/presen/raw/master/target/presen-0.1.2.jar)からダウンロードして下さい。

# インストール
- ダウンロードしたプラグインファイルをastah*アプリケーションにドラッグドロップするか、Program Files\asta-professionals\pluginsに置いて下さい。

# 機能説明
- `追加`：今の表示状態をページとして追加する
- `挿入`：今の順番に、今の表示状態をページとして挿入する
- `削除`：今のページを削除する
- `全消去`：すべてのページを削除する

- `セーブ`：すべてのページを図の定義に保存する（図の定義の文字列を上書き）
- `ロード`：すべてのページを図の定義から読み込む

- `|<`：最初のページを表示する
- `<`：前のページを表示する
- `数字`：今のページを再表示する
- `>`：次ページを表示する
- `>|`：最後のページを表示する


## 使い方
（注意点）
- プレゼンの記録は図の定義として保存されます。既存の定義は上書きされますのでご注意ください。

（準備）
- プレゼンしたい図を開く
- プレゼンプラグインを開く（プラグインタブを選択する）
- 図の拡大縮小や位置を調整して表示したい状態にする
- `追加ボタン`を押して記録する。真ん中の`数字ボタン`の数字が１つ増える。図の定義欄にプレゼンデータが保存される。
- `削除ボタン`を押して削除する。真ん中の`数字ボタン`の数字が１つ減る。図の定義欄にプレゼンデータが保存される。
- `挿入ボタン`を押して今の順番に今の状態を追加する。数字表示が１つ増える。図の定義欄にプレゼンデータが保存される。
- `セーブボタン`を押してすべてのページを図の定義欄に保存する。

（プレゼン）
- `|<ボタン`を押して最初のページを表示する。
- `<ボタン`や`>ボタン`を教えて前後のページを表示にする。
- `>|ボタン`を押して最後のページを表示する。
- `数字ボタン`を押して今のページを再表示する。（図を動かした場合とかに表示をリセットするときに使う）

（他の図を開いてプレゼン）
- `全消去ボタン`を押して前に開いていたプレゼンのすべてのページを削除する。
- `ロードボタン`を押して今の図のプレゼン情報を図の定義から読み込む。

以上
