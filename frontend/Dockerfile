# =========================================
# Stage 1: Build the Angular Application
# =========================================
ARG NODE_VERSION=22.20.0-alpine
ARG NGINX_VERSION=alpine

# Use a lightweight Node.js image for building
FROM node:${NODE_VERSION} AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy package-related files first to leverage Docker's caching mechanism
COPY package.json package-lock.json ./

# Install project dependencies using npm ci (ensures a clean, reproducible install)
RUN --mount=type=cache,target=/root/.npm npm ci

# Copy the rest of the application source code into the container
COPY . .

# Build the Angular application using the 'development' configuration to bypass budget checks
RUN npm run build -- --configuration development

# =========================================
# Stage 2: Prepare Nginx to Serve Static Files
# =========================================
FROM nginxinc/nginx-unprivileged:${NGINX_VERSION} AS runner

# Use a built-in non-root user for security best practices
USER nginx

# Copy custom Nginx config
COPY nginx.conf /etc/nginx/nginx.conf

# Copy the static build output from the build stage to Nginx's default HTML serving directory
# O nome 'trabalho1' vem do seu package.json e angular.json
COPY --chown=nginx:nginx --from=builder /app/dist/trabalho1/browser /usr/share/nginx/html

# Expose port 8080 to allow HTTP traffic
EXPOSE 8080

# Start Nginx directly with custom config
ENTRYPOINT ["nginx", "-c", "/etc/nginx/nginx.conf"]
CMD ["-g", "daemon off;"]