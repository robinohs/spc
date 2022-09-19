import vue from "@vitejs/plugin-vue";
import { defineConfig } from "vite";

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    port: 3000,
    proxy: {
      "/api": {
        target: "http://localhost:6421",
        changeOrigin: true,
        ws: true,
      },
    },
  },
  plugins: [vue()],
});
