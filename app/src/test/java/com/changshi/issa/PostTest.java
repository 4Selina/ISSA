package com.changshi.issa;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.changshi.issa.DatabaseHandler.Post;

public class PostTest {

        private Post post;

        @Before
        public void setup() {
            String title = "Test Title";
            String description = "Description";
            String bannerUrl = "test/bannerUrl";
            String category = "Learning Support";
            String heading = "Heading";
            String details = "details";
            String conclusion = "Conclusion";
            post = new Post(title, description, bannerUrl, category, heading, details, conclusion);
        }

        @Test
        public void testGetters() {
            assertEquals("Test Title", post.getTitle());
            assertEquals("Description", post.getDescription());
            assertEquals("test/bannerUrl", post.getBannerUrl());
            assertEquals("Learning Support", post.getCategory());
            assertEquals("Heading", post.getHeading());
            assertEquals("details", post.getDetails());
            assertEquals("Conclusion", post.getConclusion());
        }

        @Test
        public void testSetters() {
            post.setId(1);
//            post.setThumbUpCounts(10);
//            post.setCollectedCounts(5);

            assertEquals(1, post.getId());
//            assertEquals(10, post.getThumbUpCounts());
//            assertEquals(5, post.getCollectedCounts());
        }
    }
