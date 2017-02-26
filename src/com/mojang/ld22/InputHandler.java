package com.mojang.ld22;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements MouseListener, KeyListener {
	
	public class Key {
		public int presses, absorbs;
		public boolean down, clicked;
		
		public Key() {
			keys.add(this);
		}
		
		public void toggle(boolean pressed) {
			if (pressed != down) {
				down = pressed;
			}
			if (pressed) {
				presses++;
			}
		}
		
		public void tick() {
			if (absorbs < presses) {
				absorbs++;
				clicked = true;
			} else {
				clicked = false;
			}
		}
	}
	
	
	public class Mouse {
		
		public int pressesd, absorbsd;
		public boolean click, down;
		
		public Mouse() {
			mouse.add(this);
		}
		
		public void toggle(boolean clickd) {
			if (clickd != down) {
				down = clickd;
			}
			if (clickd) {
				pressesd++;
			}
		}
		public void tick() {
			if (absorbsd < pressesd) {
				absorbsd++;
				click = true;
			} else {
				click = false;
			}
		}
	}
	
	public List<Mouse> mouse = new ArrayList<Mouse>();
	public Mouse one = new Mouse();
	public Mouse two = new Mouse();
	public Mouse tri = new Mouse();
	
	
	public List<Key> keys = new ArrayList<Key>();
	public String lastKeyTyped = "";
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key attack = new Key();
	public Key menu = new Key();
	public Key craft = new Key();
	public Key pause = new Key();
	public Key sethome = new Key();
	public Key home = new Key();
	public Key mode = new Key();
	public Key survival = new Key();
	public Key creative = new Key();
	public Key hardcore = new Key();
	public Key fps = new Key();
	public Key options = new Key();
	public Key soundOn = new Key();
	public Key dayTime = new Key();
	public Key nightTime = new Key();
	public Key a = new Key();
	public Key b = new Key();
	public Key c = new Key();
	public Key d = new Key();
	public Key e = new Key();
	public Key f = new Key();
	public Key g = new Key();
	public Key h = new Key();
	public Key i = new Key();
	public Key j = new Key();
	public Key k = new Key();
	public Key l = new Key();
	public Key m = new Key();
	public Key n = new Key();
	public Key o = new Key();
	public Key p = new Key();
	public Key q = new Key();
	public Key r = new Key();
	public Key s = new Key();
	public Key t = new Key();
	public Key u = new Key();
	public Key v = new Key();
	public Key w = new Key();
	public Key x = new Key();
	public Key y = new Key();
	public Key z = new Key();
	public Key a1 = new Key();
	public Key a2 = new Key();
	public Key a3 = new Key();
	public Key a4 = new Key();
	public Key a5 = new Key();
	public Key a6 = new Key();
	public Key a7 = new Key();
	public Key a8 = new Key();
	public Key a9 = new Key();
	public Key a0 = new Key();
	public Key f2 = new Key();
	public Key f3 = new Key();
	
	
	public Key enter = new Key();
	public Key delete = new Key();
	public Key space = new Key();
	public Key backspace = new Key();
	
	
	public void releaseAll() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).down = false;
		}
	}
	
	public void tick() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).tick();
		}
	}
	
	public InputHandler(Game game) {
		game.addKeyListener(this);
		game.addMouseListener(this);
	}
	
	public void keyPressed(KeyEvent ke) {
		toggle(ke, true);
	}
	
	public void keyReleased(KeyEvent ke) {
		toggle(ke, false);
	}
	
	public void keyTyped(KeyEvent ke) {
		lastKeyTyped = String.valueOf(ke.getKeyChar());
	}
	
	private void toggle(KeyEvent ke, boolean pressed) {
		if (ke.getKeyCode() == KeyEvent.VK_NUMPAD8) up.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_NUMPAD2) down.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_NUMPAD4) left.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_NUMPAD6) right.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_W) up.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_S) down.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_A) left.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_D) right.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_UP) up.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_DOWN) down.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_LEFT) left.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_RIGHT) right.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_TAB) menu.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_ALT) menu.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_ALT_GRAPH) menu.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_SPACE) attack.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_CONTROL) attack.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_NUMPAD0) attack.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_INSERT) attack.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_ENTER) menu.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_ENTER) enter.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_Q) craft.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_NUMPAD1) craft.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_E) craft.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) pause.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_N) pause.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_Z) craft.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_X) menu.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_C) attack.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_R) r.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_T) t.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_1) sethome.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_H) home.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_G) mode.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_1) survival.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_2) creative.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_3) hardcore.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_F3) hardcore.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_O) options.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_S) soundOn.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_S) soundOn.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_2) dayTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_3) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_I) i.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_W) w.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_L) l.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_S) s.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_SPACE) space.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) backspace.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_F2) f2.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_F3) f3.toggle(pressed);
		
/*
		if (ke.getKeyCode() == KeyEvent.VK_A) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_B) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_C) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_D) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_E) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_F) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_G) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_H) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_I) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_J) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_K) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_L) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_M) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_N) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_O) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_P) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_Q) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_R) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_S) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_T) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_U) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_V) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_W) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_X) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_Y) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_Z) nightTime.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_1) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_2) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_3) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_A) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_A) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_A) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_A) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_A) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_A) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_A) nightTime.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_A) nightTime.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_A) nightTime.toggle(pressed);
		
*/
	}
	
	private void click(MouseEvent e, boolean clickd) {
	if (e.getButton() == MouseEvent.BUTTON1) one.toggle(clickd);
	if (e.getButton() == MouseEvent.BUTTON2) two.toggle(clickd);
	if (e.getButton() == MouseEvent.BUTTON3) tri.toggle(clickd);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
    public void mousePressed(MouseEvent e) {
 	  click(e, true);
    }
     
    public void mouseReleased(MouseEvent e) {
 	  click(e, false);
    }
}
