# Permisos y Comandos (Forge 1.20.1 / 47.4.20)

Mod **solo de servidor**: los clientes (incluso vanilla) no necesitan instalarlo.

Va solo en la carpeta `mods/` del servidor. No usa clases ni paquetes de cliente, no registra bloques/items
y declara `displayTest="IGNORE_SERVER_VERSION"`, asi que los jugadores entran con Minecraft vanilla 1.20.1
sin instalar nada. Todo lo que ven (prefijos, ping en el tab, mensajes) viaja con paquetes normales.
Ojo: si el servidor tiene OTROS mods que si necesiten estar en el cliente, los jugadores tendran que instalar esos.

## Compilar
Requiere JDK 17. Lo mas seguro es usar el MDK oficial:
1. Descarga el MDK de Forge 1.20.1-47.4.20 (files.minecraftforge.net).
2. Copia dentro del MDK `src/main/java` y `src/main/resources` de este proyecto (sustituyendo los de ejemplo).
   Si usas el `build.gradle` de este proyecto, usa tambien el wrapper de Gradle del MDK.
3. `./gradlew build` -> el jar queda en `build/libs/`.
4. Pon el jar en `mods/` del servidor.

## Archivos (se crean solos en `config/permscmds/`)
- `permissions.json` grupos y usuarios (se edita con /perm o a mano + `/perm reload`)
- `settings.json` ajustes: opsBypass, cooldown, tpa, max homes, ping en el tab (`tabShowPing`, `tabRefreshTicks`)
- `data.json` homes y spawn
- `custom_commands.json` comandos personalizados (requieren reiniciar o `/reload`)

## Rangos
- Todos los jugadores son del grupo **user** por defecto (prefijo `&a[User] ` en verde) sin tener que asignarlo.
  Cambiar el grupo por defecto: campo `defaultGroup` de `permissions.json`.
- Se crea tambien el grupo **admin** (`&c[Admin] `, hereda de user, con `permscmds.*` y `command.*`).
- Si ya tenias un `permissions.json` creado, borralo para regenerarlo o edita `defaultGroup`.
- Un jugador con otros grupos sigue heredando siempre el grupo por defecto.

## Lista de jugadores (tab)
`[Prefijo] Nombre [23ms]`, con el ping en verde (<80), amarillo (<150) o rojo. Se refresca cada 3 s
(el valor de latencia lo actualiza Minecraft aprox. cada 15 s). Se desactiva con `tabShowPing: false`.
El prefijo tambien sale en el chat.

## Permisos
- Nodos con comodin: `*`, `command.*`, `permscmds.*`.
- Negacion con `-`: `-command.kill`.
- `a.b` tambien concede/niega `a.b.c`.
- Gana la regla mas especifica; a igualdad, la negacion. Primero cuentan los permisos propios del jugador,
  luego sus grupos (por peso, con herencia) y por ultimo el grupo por defecto.
- Si un nodo no esta definido, se aplica el comportamiento normal de Minecraft.

### Comandos de Minecraft: `command.<ruta>`
- `command.gamemode` permite `/gamemode ...`; `command.gamemode.creative` solo `/gamemode creative`.
- `-command.kill` bloquea `/kill`.
- Para permitir un comando a quien no es op, el mod sube su nivel solo lo justo para ese comando.
- Cuidado: `command.execute`, `command.function`, `command.op` o `command.*` equivalen a dar casi todo.
- Los operadores nivel 4 se saltan las comprobaciones (`opsBypass`).

### Nodos del mod
| Nodo | Por defecto | Efecto |
|---|---|---|
| `permscmds.admin` | no | usar /perm |
| `permscmds.home` / `permscmds.sethome` | si | /home /homes, /sethome /delhome |
| `permscmds.spawn` | si | /spawn |
| `permscmds.setspawn` | no | /setspawn |
| `permscmds.tpa` | si | /tpa /tpaccept |
| `permscmds.back` | si | /back |
| `permscmds.ping` / `permscmds.ping.others` | si / si | /ping [jugador] |
| `permscmds.fly` / `.others` | no | /vuelo (/fly) [jugador] |
| `permscmds.god` / `.others` | no | /god [jugador] |
| `permscmds.heal` / `.others` | no | /heal [jugador] |
| `permscmds.feed` / `.others` | no | /feed [jugador] |
| `permscmds.nocooldown` | no | sin espera entre teletransportes |
| `permscmds.homes.<n>` / `permscmds.homes.unlimited` | - | maximo de homes |

## Comandos
```
/perm groups | creategroup <n> | deletegroup <n>
/perm group <g> info | add|remove <permiso> | parent add|remove <g2> | prefix <texto &c|none> | weight <n>
/perm user <jugador> info | add|remove <permiso> | setgroup|addgroup|removegroup <grupo>
/perm check <jugador> <permiso>      /perm reload

/sethome [n]  /home [n]  /delhome <n>  /homes  /spawn  /setspawn
/back          vuelve a donde estabas antes del ultimo teletransporte o muerte (repetirlo alterna)
/tpa <jugador>  /tpaccept  /tpdeny
/vuelo (/fly) [jugador]   activa o desactiva el vuelo
/ping [jugador]   /heal [jugador]   /feed [jugador]   /god [jugador]
```

## Comandos personalizados (ejemplo)
```json
{ "commands": [
  { "name": "reglas", "permission": "", "runAs": "player",
    "messages": ["&6Reglas", "&f1. Respeta a todos"], "commands": [] },
  { "name": "curar", "permission": "custom.curar", "runAs": "console",
    "messages": ["&aCurado"], "commands": ["effect give {player} minecraft:instant_health 1 1"] }
]}
```

## Notas
- Los comandos que solo se pueden usar gracias a un permiso (no por ser op) funcionan al escribirlos, pero pueden no salir en el autocompletado.
- Si en el servidor tienes plugins de Bukkit (Arclight) con comandos iguales (/home, /spawn, /back...), pueden chocar con estos.
- El vuelo y el modo dios no se aplican en creativo/espectador (ya los tienen).
