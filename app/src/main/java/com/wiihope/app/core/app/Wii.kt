package com.wiihope.app.core.app

object Wii {
    const val app = "WiiHope"
    const val id = "wiihope"
    const val packageName = "com.wiihope.app"
    const val repo = "wiihopee"
    const val desc = "Biblia Audio, musica cristiana, oracion y citas de fe"
    const val lanzamiento = 2026
    const val by = "@wilder.taype"
    const val link = "https://wtaype.github.io/"
    const val version = "v10"
}

/*
Version workflow reference:
1. Main update:
git add . ; git commit -m "Actualizacion Principal v10.0.0" ; git push origin main
2. New version tag:
git tag v10 -m "Version v10" ; git push origin v10
3. Emergency tag replacement:
git tag -d v10 ; git tag v10 -m "Version v10 actualizada" ; git push origin v10 --force

git init
git add .
git commit -m "El mejor proyecto wiihope"
git branch -M main
git remote add origin https://github.com/wtaype/wiihopee.git  
git push origin main
*/
