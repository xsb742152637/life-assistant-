'use strict'
const utils = require('./utils')
const config = require('../config')
const isProduction = process.env.NODE_ENV === 'production'
const sourceMapEnabled = isProduction
  ? config.build.productionSourceMap
  : config.dev.cssSourceMap

module.exports = {
  loaders: utils.cssLoaders({
    sourceMap: sourceMapEnabled,
    extract: isProduction
  }),
    // 第三方插件配置
    pluginOptions: {
        // 导入全局的less变量
        'style-resources-loader': {
            preProcessor: 'less',
            //在assets（静态资源文件夹）下创建全局样式文件；index.less 它就是less全局变量
            patterns: [require('path').resolve('./src/assets/style/index.less')]
        }
    },
  cssSourceMap: sourceMapEnabled,
  cacheBusting: config.dev.cacheBusting,
  transformToRequire: {
    video: ['src', 'poster'],
    source: 'src',
    img: 'src',
    image: 'xlink:href'
  }
}
