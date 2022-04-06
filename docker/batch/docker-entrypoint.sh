#!/bin/bash

# Paramètres par défaut du conteneur
export ABESHELLO_BATCH_CRON=${ABESHELLO_BATCH_CRON:='0 * * * *'}
export ABESHELLO_BATCH_AT_STARTUP=${ABESHELLO_BATCH_AT_STARTUP:='1'}

# Réglage de /etc/environment pour que les crontab s'exécutent avec les bonnes variables d'env
echo "$(env)
LANG=en_US.UTF-8" > /etc/environment

# Charge la crontab depuis le template
envsubst < /etc/cron.d/tasks.tmpl > /etc/cron.d/tasks
echo "-> Installation des crontab :"
cat /etc/cron.d/tasks
crontab /etc/cron.d/tasks

# Force le démarrage du batch au démarrage du conteneur
if [ "$ABESHELLO_BATCH_AT_STARTUP" = "1" ]; then
  echo "-> Lancement de abes-hello-batch1.sh au démarrage du conteneur"
  /scripts/abes-hello-batch1.sh
fi

# execute CMD (crond)
exec "$@"
