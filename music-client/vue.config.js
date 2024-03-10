const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  chainWebpack: config => {
    config.plugin('define').tap(definitions => {
        Object.assign(definitions[0]['process.env'], {
          NODE_HOST: '"http://47.96.95.29/api"',
          //   NODE_HOST: '"https://music-backend.onlyicanstopmyself.top"',
        });
        return definitions;
    });
  }
})
