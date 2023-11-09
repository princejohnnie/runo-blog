<script setup>
import ArticleMain from '@/components/article/ArticleMain.vue';
import ArticleCard from '@/components/article/ArticleCard.vue';
import ArticleEditorCard from '@/components/article/ArticleEditorCard.vue';
import HomeCategories from '@/components/homepage/HomeCategories.vue';

</script>

<template>
    <main>
        <ArticleMain />

        <section class="section section__popularTopics">
        <div class="section__inner">

            <HomeCategories />

            <div class="articles">
                <ArticleCard v-for="(article, index) in articles" :key="index" :article="article" />
                </div>
            </div>

        </section>

    <section class="section section__editorPicks">
        <div class="section__inner">
            <h2 class="section__heading">Editor's Pick</h2>
            <div class="editorArticles">

                <ArticleEditorCard v-for="(editorArticle, index) in editorArticles" :key="index" :editorArticle="editorArticle" />
                </div>
            </div>
        </section>

    </main>
</template>

<script>
export default {
    data() {
        return {
            articles: [
                {
                    image: "/images/public_article_image1.jpeg",
                    categories: [
                        "ADVENTURE"
                    ],
                    isPremium: true,
                    title: "Dream destinations to visit this year in Paris",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
                    time: "08.08.2021",
                    name: "Jennifer Lawrence",
                    description: "Thinker & Designer",
                    authorImage: "/images/public_article_author_image1.jpeg"
                },
                {
                    image: "/images/public_article_image2.jpeg",
                    categories: [
                        "BRAND",
                        "FASHION"
                    ],
                    isPremium: true,
                    title: "Richird Norton photorealistic rendering as real photos",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
                    time: "08.08.2021",
                    name: "Jennifer Lawrence",
                    description: "Designer",
                    authorImage: "/images/public_article_author_image2.jpeg"
                },
                {
                    image: "/images/public_article_image3.jpeg",
                    categories: [
                        "ADVENTURE"
                    ],
                    isPremium: false,
                    title: "Dream destinations to visit this year in Paris",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
                    time: "08.08.2021",
                    name: "Sarah Lawrence",
                    description: "Journalist",
                    authorImage: "/images/public_article_author_image3.jpeg"
                },
                {
                    image: "/images/public_article_image4.jpeg",
                    categories: [
                        "ADVENTURE"
                    ],
                    isPremium: false,
                    title: "Dream destinations to visit this year in Paris",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
                    time: "08.08.2021",
                    name: "Jennifer Lawrence",
                    description: "Thinker & Designer",
                    authorImage: "/images/public_article_author_image1.jpeg"
                },
                {
                    image: "/images/public_article_image5.jpeg",
                    isPremium: false,
                    title: "Art of Seasons: 40+ Bright Illustrations by Nature",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
                    time: "08.08.2021",
                    name: "David Tomas",
                    description: "Thinker",
                    authorImage: "/images/public_article_author_image5.jpeg"
                },
                {
                    image: "/images/public_article_image6.jpeg",
                    categories: [
                        "FASHION"
                    ],
                    isPremium: false,
                    title: "The Anatomy of a Web Page and Basic Elements",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
                    time: "08.08.2021",
                    name: "Jennifer Lawrence",
                    description: "Thinker & Designer",
                    authorImage: "/images/public_article_author_image1.jpeg"
                },
                {
                    image: "/images/public_article_image7.jpeg",
                    isPremium: false,
                    title: "Types of Contrast in User Interface Design",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
                    time: "08.08.2021",
                    name: "Andrey Edison",
                    description: "Thinker & Designer",
                    authorImage: "/images/public_article_author_image7.jpeg"
                },
                {
                    image: "/images/public_article_image8.jpeg",
                    isPremium: false,
                    title: "Dream destinations to visit this year in Paris",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
                    time: "08.08.2021",
                    name: "Sean Anderson",
                    description: "Thinker & Designer",
                    authorImage: "/images/public_article_author_image8.jpeg"
                }
            ],
            editorArticles: [
                {
                    image: "/images/editor_article_bg1.jpeg",
                    categories: [
                        "FASHION"
                    ],
                    isPremium: true,
                    time: "08.08.2021",
                    title: "Richird Norton photorealistic rendering as real photos",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data."
                },
                {
                    image: "/images/editor_article_bg2.jpeg",
                    categories: [
                        "FASHION"
                    ],
                    isPremium: false,
                    time: "08.08.2021",
                    title: "Richird Norton photorealistic rendering as real photos",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data."
                },
                {
                    image: "/images/editor_article_bg3.jpeg",
                    categories: [
                        "FASHION"
                    ],
                    isPremium: false,
                    time: "08.08.2021",
                    title: "Richird Norton photorealistic rendering as real photos",
                    content: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data."
                }
            ]
        }
    },
}
</script>
