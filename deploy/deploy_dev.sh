#!/bin/bash

RESOURCE_GROUP="cash-flow-resource-group"
APP_NAME="cash-flow-backend"
IMAGE="cashflowregistry.azurecr.io/cash-flow:v1.0"
ENV_NAME="my-container-env"

# Check if the app exists
EXISTING_APP=$(az containerapp show --name $APP_NAME --resource-group $RESOURCE_GROUP --query "name" --output tsv 2>/dev/null)

if [ -z "$EXISTING_APP" ]; then
  echo "🚀 App does not exist. Deploying a new one..."
#  az containerapp create \
#    --name $APP_NAME \
#    --resource-group $RESOURCE_GROUP \
#    --image $IMAGE \
#    --environment $ENV_NAME \
#    --target-port 8080 \
#    --ingress external \
#    --registry-server cashflowregistry.azurecr.io
else
  echo "🔄 App exists. Updating with new image..."
#  az containerapp update \
#    --name $APP_NAME \
#    --resource-group $RESOURCE_GROUP \
#    --image $IMAGE
fi
